default:
	@$(MAKE) -s build

build:
	@mvn clean package

start:
# HEAP_SIZE parameter: Java heap size, for instace 1024k, 512m, 8g.
# We set the intial heap size (-Xms<size>) as well as the maximum heap
# size (-Xmx<size) to avoid overhead when it will need more heap space.
#
# Usage example: make start HEAP_SIZE=1g
ifndef HEAP_SIZE
	$(info Starting with 1G of heap size...)
	@java -Xms1g -Xmx1g -jar target/stemmer-1.0-SNAPSHOT.jar
else
	$(info Starting with ${HEAP_SIZE} of heap size...)
	@java -Xms${HEAP_SIZE} -Xmx${HEAP_SIZE} -jar target/stemmer-1.0-SNAPSHOT.jar
endif

start-time:
	@time $(MAKE) -s start

javadoc:
	$(info Creating Java documentation...)
	@cd src/main/java && \
	javadoc -d ../../../docs/ -subpackages com.stemby && \
	printf "\nJava documentation created in docs/\n"
