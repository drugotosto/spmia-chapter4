#!/usr/bin/env bash
echo "Vado sia a TAGGARE che a creare una nuova GITHUB RELEASE del source code relativo al repository. Come TAG utilizzo: $BUILD_NAME"

#echo "Successivemante faccio la push di tale tag anche in remoto"
#git push https://github.com/drugotosto/spmia-chapter4 $BUILD_NAME

export TARGET_URL="https://api.github.com/repos/drugotosto/spmia-chapter4/releases"

body="{
  \"tag_name\": \"$BUILD_NAME\",
  \"target_commitish\": \"master\",
  \"name\": \"$BUILD_NAME\",
  \"body\": \"Release of version $BUILD_NAME\",
  \"draft\": true,
  \"prerelease\": true
}"

curl -k -X POST -H "Content-Type: application/json" -H "Authorization: token $GITHUB_TOKEN" -d "$body" $TARGET_URL
