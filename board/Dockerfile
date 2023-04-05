FROM node:18-alpine AS builder

RUN mkdir /app

COPY . /app

RUN cd /app && yarn install && \
  yarn build 

FROM node:18-alpine

RUN mkdir /app

COPY --from=builder /app/build /app/build
COPY --from=builder /app/package.json /app/yarn.lock /app/

RUN cd /app && \
  yarn install --production && \
  yarn cache clean

WORKDIR /app

ENV PORT=80
CMD ["node", "build/index.js"]