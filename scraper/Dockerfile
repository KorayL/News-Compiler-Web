FROM python:3.12-slim-bullseye

WORKDIR /app

# Copy the entire applicaiton and install dependencies
COPY ./ /app
RUN pip install -r requirements.txt

CMD []
ENTRYPOINT ["/bin/bash", "runner.sh"]
