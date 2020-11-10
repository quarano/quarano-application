# /bin/bash
set -e

## Please start this script from project-root, e.g. ./deployment/deploy-stage.sh

mvn -B clean package --file backend/pom.xml
npm run build-stage --prefix frontend/
cf push quarano-backend-dev -f backend/manifest.yml
cf push quarano-frontend-dev -f frontend/manifest.yml

