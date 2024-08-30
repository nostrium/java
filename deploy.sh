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

# Step 2: Copy the JAR file to the remote server using scp
echo "Copying JAR to remote server..."
scp "$FAT_JAR" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR" || { echo "SCP failed"; exit 1; }

# Step 3: Stop all running Java processes
echo "Stopping all running Java processes..."
ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
    PIDS=\$(pgrep -f java)
    if [ ! -z "\$PIDS" ]; then
        echo "Killing the following Java processes: \$PIDS"
        kill \$PIDS
        while pgrep -f java >/dev/null; do
            echo "Waiting for all Java processes to terminate..."
            sleep 1
        done
        echo "All Java processes have been terminated."
    else
        echo "No Java processes running."
    fi
EOF

# Step 4: Launch the JAR file inside a screen session on the remote server
echo "Launching JAR in screen session on remote server..."
ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
    cd "$REMOTE_DIR" || { echo "Remote directory not found"; exit 1; }
    screen -S "$SCREEN_SESSION_NAME" -dm java -jar "$(basename $FAT_JAR)"
EOF

echo "Deployment completed successfully"
