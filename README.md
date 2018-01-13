# Broadcast-Service

Implementation of a broadcast service in a distributed system.

Assume a distributed system in which nodes are arranged in a certain topology (specified in a configuration file). Build a spanning tree using a distributed algorithm. Once the spanning tree construction completes, each node would know which subset of its neighbors are also its tree neighbors.
Use the spanning tree constructed above to implement a broadcast service that allows any node to send a message to all nodes in the system. The broadcast service should inform the source node of the completion of the broadcast operation. Note that, at any given time, there may be multiple broadcast operations in progress concurrently.

Output: Each node should print its set of tree neighbors. Each node should also output any broadcast message it sends or receives.
