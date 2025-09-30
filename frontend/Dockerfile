FROM node:20-alpine AS build
# Add build arguments
WORKDIR /frontend
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build
FROM nginx:1.25.4-alpine
COPY --from=build /frontend/dist/stug-appen/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
# Run nginx in foreground
CMD ["nginx", "-g", "daemon off;"]
