#!/bin/bash

# ASCII Art for Nostrium
echo "_  _ ____ ____ ___ ____ _ _  _ _  _ "
echo "|\\ | |  | [__   |  |__/ | |  | |\\/| "
echo "| \\| |__| ___]  |  |  \\ | |__| |  | "
echo ""

# Variables
PROJECT_DIR=$(pwd)
TARGET_DIR="$PROJECT_DIR/target"
REMOTE_USER="root"
REMOTE_HOST="128.140.43.202"
REMOTE_DIR="/opt/nostrium"
SCREEN_SESSION_NAME="nostrium_session"

# Step 0: Check if Maven is installed, install if not
if ! mvn -version > /dev/null 2>&1; then
    echo "Maven not found, installing..."
    sudo apt-get update
    sudo apt-get install -y maven
else
    echo "Maven is already installed."
fi

# Step 0.1: Check if sshpass is installed, install if not
if ! sshpass -V > /dev/null 2>&1; then
    echo "sshpass not found, installing..."
    sudo apt-get update
    sudo apt-get install -y sshpass
else
    echo "sshpass is already installed."
fi

# Step 1: Compile the Java Maven project as a fat JAR
echo "Building Maven project..."
mvn clean package || { echo "Maven build failed"; exit 1; }

# Debug information: list files in target directory
echo "Contents of $TARGET_DIR after Maven build:"
ls -l $TARGET_DIR

# Check for the shaded JAR first, then fall back to the main JAR if not found
FAT_JAR=$(find "$TARGET_DIR" -name "*-shaded.jar" -o -name "nostrium-1.0.0.jar" | grep -v "original")
if [ -z "$FAT_JAR" ]; then
    echo "Fat JAR file not found in $TARGET_DIR"
    exit 1
else
    echo "Found Fat JAR: $FAT_JAR"
fi

# Step 2: Prompt for the SSH password (if needed)
# If no password is provided, the script will attempt to use SSH keys for login.
echo "Enter SSH password for $REMOTE_USER@$REMOTE_HOST (press Enter to use SSH key-based login):"
read -s SSH_PASSWORD

# Step 3: Ensure the remote directory exists
echo "Creating directory $REMOTE_DIR on remote server..."
if [ -z "$SSH_PASSWORD" ]; then
    # Use SSH key-based login if no password is provided
    ssh -o StrictHostKeyChecking=no "$REMOTE_USER@$REMOTE_HOST" << EOF
        mkdir -p "$REMOTE_DIR"
EOF
else
    # Use password-based login
    sshpass -p "$SSH_PASSWORD" ssh -o StrictHostKeyChecking=no "$REMOTE_USER@$REMOTE_HOST" << EOF
        mkdir -p "$REMOTE_DIR"
EOF
fi

# Step 4: Copy the JAR file to the remote server using scp
echo "Copying JAR to remote server..."
if [ -z "$SSH_PASSWORD" ]; then
    scp "$FAT_JAR" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR" || { echo "SCP failed"; exit 1; }
else
    sshpass -p "$SSH_PASSWORD" scp "$FAT_JAR" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR" || { echo "SCP failed"; exit 1; }
fi

# Step 5: Stop any running instances of the same JAR file
echo "Stopping any running instances of the JAR..."
if [ -z "$SSH_PASSWORD" ]; then
    ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
        pkill -f "$REMOTE_DIR/$(basename $FAT_JAR)"
EOF
else
    sshpass -p "$SSH_PASSWORD" ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
        pkill -f "$REMOTE_DIR/$(basename $FAT_JAR)"
EOF
fi

# Step 6: Launch the JAR file inside a screen session on the remote server
echo "Launching JAR in screen session on remote server..."
if [ -z "$SSH_PASSWORD" ]; then
    ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
        cd "$REMOTE_DIR" || { echo "Remote directory not found"; exit 1; }
        screen -S "$SCREEN_SESSION_NAME" -dm java -jar "$(basename $FAT_JAR)"
EOF
else
    sshpass -p "$SSH_PASSWORD" ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
        cd "$REMOTE_DIR" || { echo "Remote directory not found"; exit 1; }
        screen -S "$SCREEN_SESSION_NAME" -dm java -jar "$(basename $FAT_JAR)"
EOF
fi

echo "Deployment completed successfully"
