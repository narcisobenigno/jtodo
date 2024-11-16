.PHONY: run
run:
	@./service/gradlew -p ./service run

.PHONY: clean
clean:
	@./service/gradlew -p ./service clean

.PHONY: lint
lint:
	@./service/gradlew -p ./service ktlintCheck

.PHONY: test
test:
	@./service/gradlew -p ./service test

.PHONY: test.all
test.all: test

.PHONY: git.%
git.%:
	git $*

.PHONY: ci
ci: git.pull clean test.all git.push

