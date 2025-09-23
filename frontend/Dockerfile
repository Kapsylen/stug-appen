FROM bitnami/node AS build
# Add build arguments
ARG IMAGE_TAG=stug-web
WORKDIR /frontend
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
FROM nginx:1.25.4-alpine
COPY --from=build /frontend/dist/stug-appen/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80

