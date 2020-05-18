FROM gmathieu/node-browsers:3.0.0 AS build

COPY frontend/package.json /usr/angular-workdir/
WORKDIR /usr/angular-workdir

COPY ./frontend/ /usr/angular-workdir

ARG ENV=prod
RUN npm run build-$ENV

FROM nginx:1.15.8-alpine

## Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

COPY ./frontend/dev/nginx.conf /etc/nginx/nginx.conf

COPY --from=build  /usr/angular-workdir/dist/coronareportfrontend /usr/share/nginx/html

RUN echo "nginx -g 'daemon off;'" > run.sh

ENTRYPOINT ["sh", "run.sh"]
