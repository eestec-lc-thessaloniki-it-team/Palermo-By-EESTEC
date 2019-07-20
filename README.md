# Build
mvn clean package && docker build -t eestec.thessaloniki/palermo .

# RUN

docker rm -f palermo || true && docker run -d -p 8080:8080 -p 4848:4848 --name palermo eestec.thessaloniki/palermo 