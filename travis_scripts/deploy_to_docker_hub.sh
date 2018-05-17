#!/usr/bin/env bash
echo "Dopo essermi loggato sul registry Docker HUB vado a pushare le diverse Docker images."
echo "Ognuna delle diverse immagini relative ai vari microservizi oltre ad essere già taggata con 'LocalBuild-Latest' vengono anche taggate con '<nomeBranch_dataOra_NumeroBuildTravis'"

docker login -u $DOCKER_USERNAME -p $DOCKER_PASSWORD

docker tag drugotosto84/tmx-licensing-service:LocalBuild-Latest drugotosto84/tmx-licensing-service:$BUILD_NAME
docker push drugotosto84/tmx-licensing-service:$BUILD_NAME
docker push drugotosto84/tmx-licensing-service:LocalBuild-Latest

docker tag drugotosto84/tmx-organization-service:LocalBuild-Latest drugotosto84/tmx-organization-service:$BUILD_NAME
docker push drugotosto84/tmx-organization-service:$BUILD_NAME
docker push drugotosto84/tmx-organization-service:LocalBuild-Latest

docker tag drugotosto84/tmx-confsvr:LocalBuild-Latest drugotosto84/tmx-confsvr:$BUILD_NAME
docker push drugotosto84/tmx-confsvr:$BUILD_NAME
docker push drugotosto84/tmx-confsvr:LocalBuild-Latest

docker tag drugotosto84/tmx-eurekasvr:LocalBuild-Latest drugotosto84/tmx-eurekasvr:$BUILD_NAME
docker push drugotosto84/tmx-eurekasvr:$BUILD_NAME
docker push drugotosto84/tmx-eurekasvr:LocalBuild-Latest

docker tag drugotosto84/tmx-zuulsvr:LocalBuild-Latest drugotosto84/tmx-zuulsvr:$BUILD_NAME
docker push drugotosto84/tmx-zuulsvr:$BUILD_NAME
docker push drugotosto84/tmx-zuulsvr:LocalBuild-Latest
