FROM nginx:alpine

COPY . /usr/share/nginx/html

COPY ngniz.conf /etc/nginx/conf.d/default.conf

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]
