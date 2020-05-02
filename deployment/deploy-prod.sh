# /bin/bash
set -e

## Please start this script from project-root, e.g. ./deployment/deploy-prod.sh

mvn -B clean package --file backend/pom.xml
npm run build-prod --prefix frontend/
cf push quarano-backend
cf push quarano-frontend

