#!/bin/bash

set -e

# Define variables
VERSION="11.0.23"
BASE_URL="https://repo1.maven.org/maven2/org/eclipse/jetty"

# Define artifact coordinates
ARTIFACTS=(
    "jetty-server"
    "jetty-servlet"
    "websocket/websocket-server"
)

# Function to download and install an artifact
download_and_install_artifact() {
  local groupId="org.eclipse.jetty"
  local artifactId=$1
  local version=$2

  # Replace slashes in artifactId for the file path (for websocket artifacts)
  local artifactPath=${artifactId//\//\/}
  local artifactPom="${artifactId}-${version}.pom"
  local artifactJar="${artifactId}-${version}.jar"
  
  echo "Downloading ${artifactId} JAR and POM..."

  wget -q --retry-connrefused --waitretry=1 --read-timeout=20 --timeout=15 -t 3 -O "${artifactJar}" "${BASE_URL}/${artifactPath}/${version}/${artifactJar}" || {
    echo "Failed to download ${artifactJar} after 3 attempts."
    exit 1
  }

  wget -q --retry-connrefused --waitretry=1 --read-timeout=20 --timeout=15 -t 3 -O "${artifactPom}" "${BASE_URL}/${artifactPath}/${version}/${artifactPom}" || {
    echo "Failed to download ${artifactPom} after 3 attempts."
    exit 1
  }

  echo "Installing ${artifactId} into local Maven repository..."
  mvn install:install-file -Dfile="${artifactJar}" -DpomFile="${artifactPom}"
  
  echo "${artifactId} installed successfully."
}

# Special case for websocket-servlet
download_and_install_websocket_servlet() {
  local artifactId="websocket-servlet"
  local artifactPom="${artifactId}-${VERSION}.pom"
  local artifactJar="${artifactId}-${VERSION}.jar"
  local url="https://repo1.maven.org/maven2/org/eclipse/jetty/websocket/websocket-servlet/${VERSION}"

  echo "Downloading ${artifactId} JAR and POM..."

  wget -q --retry-connrefused --waitretry=1 --read-timeout=20 --timeout=15 -t 3 -O "${artifactJar}" "${url}/${artifactJar}" || {
    echo "Failed to download ${artifactJar} after 3 attempts."
    exit 1
  }

  wget -q --retry-connrefused --waitretry=1 --read-timeout=20 --timeout=15 -t 3 -O "${artifactPom}" "${url}/${artifactPom}" || {
    echo "Failed to download ${artifactPom} after 3 attempts."
    exit 1
  }

  echo "Installing ${artifactId} into local Maven repository..."
  mvn install:install-file -Dfile="${artifactJar}" -DpomFile="${artifactPom}"
  
  echo "${artifactId} installed successfully."
}

# Loop through artifacts and download/install each
for artifact in "${ARTIFACTS[@]}"; do
  download_and_install_artifact "${artifact}" "${VERSION}"
done

# Special case: install websocket-servlet
download_and_install_websocket_servlet

echo "All Jetty dependencies have been installed successfully."
