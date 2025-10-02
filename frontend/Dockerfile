FROM node:20-alpine AS build
# Add build arguments
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
FROM nginx:1.25.4-alpine
USER root
RUN mkdir -p /var/cache/nginx /var/run /var/log/nginx \
    && chown -R 1001:0 /var/cache/nginx /var/run /var/log/nginx /usr/share/nginx/html /etc/nginx/conf.d \
    && chmod -R g+rwX /var/cache/nginx /var/run /var/log/nginx /usr/share/nginx/html /etc/nginx/conf.d
COPY --from=build /app/dist/stug-appen/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
USER 1001
EXPOSE 80
# Run nginx in foreground
CMD ["nginx", "-g", "daemon off;"]
