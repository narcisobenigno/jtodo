.PHONY: run
run: 
	@./service/gradlew -p ./service run

.PHONY: clean
clean:
	@./service/gradlew -p ./service clean

.PHONY: lint
lint: clean
	@./service/gradlew -p ./service ktlintCheck

.PHONY: test
test: clean
	@./service/gradlew -p ./service test

test.all: test
