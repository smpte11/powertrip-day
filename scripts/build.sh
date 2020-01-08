#!/bin/bash

images=$(echo $IMAGES | tr " " "\n")

for image in $images
do
  IFS=':' read -ra imageSplit <<< "$image"
  IMGNAME=${imageSplit[0]}
  IMGTAG=${imageSplit[1]}
  PROJECT="$(basename $IMGNAME)"
  if [ "$PUSH_IMAGE" == "true" ]
  then
    sbt "set version := \"$IMGTAG\"" "docker:publish"
    # Have to tag latest or my deployment will error when pulling the image
    docker tag "$IMGNAME:$IMGTAG" "$IMGNAME:latest"
    docker push "$IMGNAME:latest"
  else
    sbt "set version := \"$IMGTAG\"" "docker:publishLocal"
  fi
done
