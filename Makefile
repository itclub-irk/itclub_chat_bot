.EXPORT_ALL_VARIABLES:

APP_NAME   = itclub_chat_bot
VER_MAJOR  = 2
VER_MINOR  = 0
MAIN_CLASS = itbot.main

JAR_NAME = itbot.jar
UBER_JAR = target/${JAR_NAME}

# # #
PROD_HOST = XXX_myhost_XXX
PROD_PATH = /app/itbot
# # #

.PHONY: build clean dev deploy
SHELL = bash


dev:
	set -a && source .env && clojure -M:dev:nrepl


build: clean
	@clj -T:build uberjar


deploy:
	chmod g+r ${UBER_JAR}
	scp ${UBER_JAR} ${PROD_HOST}:${PROD_PATH}


clean:
	clj -T:build clean


# https://github.com/liquidz/antq/blob/main/CHANGELOG.adoc
outdated:
	@(clojure -Sdeps '{:deps {antq/antq {:mvn/version "2.11.1264"}}}' -T antq.core/-main || exit 0)

#.
