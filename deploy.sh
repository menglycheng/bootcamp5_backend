# build a docker image
docker build -t checkme .

# run the docker image
docker run -d -p 8080:8080 checkme
