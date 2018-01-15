# Broadcast-Service

Implementation of a broadcast service in a distributed system.

Assume a distributed system in which nodes are arranged in a certain topology (specified in a configuration file). Build a spanning tree using a distributed algorithm. Once the spanning tree construction completes, each node would know which subset of its neighbors are also its tree neighbors.
Use the spanning tree constructed above to implement a broadcast service that allows any node to send a message to all nodes in the system. The broadcast service should inform the source node of the completion of the broadcast operation. Note that, at any given time, there may be multiple broadcast operations in progress concurrently.

Output: Each node should print its set of tree neighbors. Each node should also output any broadcast message it sends or receives.

Sample executing result:
{csgrads1:~} source launcher.sh 
5
dc35		3332	2 4 5
dc36		5678	1 3
dc37		5231	2 4 5
dc38		2311	1 3 5
dc39		3124	1 3 4

Constructing the spanning tree...
Node2's tree neighbor: 1 3 
Node1's tree neighbor: 2 4 5 
Node4's tree neighbor: 1 
Node3's tree neighbor: 2 
Node5's tree neighbor: 1 


Node1 broadcast message: "44	2017-07-11 19:47:50"
Node2 receives message "44	2017-07-11 19:47:50" from Node1
Node4 receives message "44	2017-07-11 19:47:50" from Node1
Node5 receives message "44	2017-07-11 19:47:50" from Node1
Node2 forwards message "44	2017-07-11 19:47:50" from Node1 to Node3
Node3 receives message "44	2017-07-11 19:47:50" from Node2
Node1 receives ACK message from Node4
Node2 receives ACK message from Node3
Node2 forwards ACK message from Node3 to Node1
Node1 receives ACK message from Node5
Node1 receives ACK message from Node2


Node2 broadcast message: "84	2017-07-11 19:47:54"
Node1 receives message "84	2017-07-11 19:47:54" from Node2
Node3 receives message "84	2017-07-11 19:47:54" from Node2
Node1 forwards message "84	2017-07-11 19:47:54" from Node2 to Node4
Node4 receives message "84	2017-07-11 19:47:54" from Node1
Node1 forwards message "84	2017-07-11 19:47:54" from Node2 to Node5
Node5 receives message "84	2017-07-11 19:47:54" from Node1
Node2 receives ACK message from Node3
Node1 receives ACK message from Node4
Node1 forwards ACK message from Node4 to Node2
Node2 receives ACK message from Node1
Node1 receives ACK message from Node5
Node1 forwards ACK message from Node5 to Node2
Node2 receives ACK message from Node1
