# https://sreyaj.dev/deploy-nodejs-applications-on-a-vps-using-coolify-with-dockerfile
# Dockerfile for Next.js
FROM node:20-alpine

######
### Add your ENV Vars via ARG's here
######

# https://stackoverflow.com/a/65443098/4034811
WORKDIR /app
# starts in repo root
COPY ./web /app

# Install dependencies (peer deps for next 15 needed atm)
RUN npm install --legacy-peer-deps

# Build the Next.js application
RUN npm run build

# Expose port (default for Next.js)
EXPOSE $PORT

# Start the Next.js application
CMD ["npm", "run", "start"]
