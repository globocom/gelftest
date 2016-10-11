#Run

change the ghost in gelftest.sh 


field | description
------- | ---------------- 
protocol  | udp or tcp
big_stack  | if true, generate stackstrace with 2500 caracters, else 160
total_msg   | total msg each threads will process 
total_threads | total threads created to process msgs
tag | use to verify msg in graylog, query 'teste:<tag>'

```
mvn install 
chmod +x gelftest.sh

gelftest.sh <protocol> <big_stack> <total_msg> <total_threads> <tag>

```
