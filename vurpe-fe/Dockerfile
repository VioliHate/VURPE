# Stage 1: Build
FROM node:22-alpine AS build
WORKDIR /app

# Copia i file per le dipendenze
COPY package*.json ./
RUN npm install

# Copia tutto il codice
COPY . .

# Esegue la build (Angular creerà la cartella dist/vurpe-fe/browser)
RUN npm run build -- --configuration=production

# Stage 2: Serve con Nginx
FROM nginx:alpine

# Rimuovi i file di default di nginx
RUN rm -rf /usr/share/nginx/html/*

# COPIA i file compilati. 
# Nota: "vurpe-fe/browser" è il percorso standard del nuovo builder
COPY --from=build /app/dist/vurpe-fe/browser /usr/share/nginx/html

# Copia la configurazione di Nginx (crea questo file se non l'hai fatto!)
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]