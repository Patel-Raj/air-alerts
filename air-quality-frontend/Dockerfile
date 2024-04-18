FROM node:alpine as build

WORKDIR /app

COPY package*.json ./

ARG REACT_APP_BACKEND_BASE_URL=http://localhost:8080

ENV REACT_APP_BACKEND_BASE_URL $REACT_APP_BACKEND_BASE_URL

RUN npm install

COPY . .

RUN npm run build

FROM nginx:alpine

COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]