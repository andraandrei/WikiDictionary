# FROM node:18.12.1 as node

# COPY package-lock.json package-lock.json
# RUN npm install -g @angular/cli 
# # RUN npm install -g @angular-devkit/build-angular

# RUN mkdir -p /app
# WORKDIR /app
# # #COPY package.json /app
# # #RUN npm i https://github.com/Qix-/node-error-ex
# # #RUN npm install npm@9.2.0
# COPY . /app

# RUN ng build --prod
FROM nginx:alpine
COPY ./dist /usr/share/nginx/html
