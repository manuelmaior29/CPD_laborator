from enum import auto
import socket, sys, time, pika
from threading import Thread

### State
user_id_global = sys.argv[1]

### Constants
HOST_IP = "127.0.0.1"
TOPIC_SUBSCRIPTION_DICTIONARY = {
    "1": ["sports", "tv-shows", "gaming"],
    "2": ["sports", "gaming"],
    "3": ["sports", "tv-shows"]
}

### Functions

# Callback function for consuming messages
def message_callback(ch, method, properties, body):
    split_routing_key = method.routing_key.split(".")
    
    message = body.decode("utf-8")
    topic = split_routing_key[0]
    from_id = split_routing_key[1]

    is_self_id = from_id == user_id_global
    if (not is_self_id):
        print(f"[{topic}] <User{from_id}>: {message}")

# Main function
def main(user_id, port_left, port_right):
    print("User ", user_id, " started.")

    # decide if user has token at start time
    holds_token = False
    if(user_id == "1"):
        holds_token = True

    # print subscriptions
    print('Subscriptions: ', TOPIC_SUBSCRIPTION_DICTIONARY[user_id])

    # establish pubsub connection
    pubsub_params = pika.URLParameters("amqps://aqbjmppo:EXLk7KRx3uJGx_yo0AUbjxWcLf3dzdG0@cow.rmq2.cloudamqp.com/aqbjmppo")
    pubsub_connection = pika.BlockingConnection(parameters=pubsub_params)
    pubsub_channel = pubsub_connection.channel()
    pubsub_channel.exchange_declare(exchange="cpdtopicex", exchange_type='topic', durable=True)

    for topic in TOPIC_SUBSCRIPTION_DICTIONARY[user_id]:
        queue_declaration = pubsub_channel.queue_declare(f"{topic}.{user_id}", exclusive=True)
        queue_name = queue_declaration.method.queue
        pubsub_channel.queue_bind(exchange="cpdtopicex", queue=queue_name, routing_key=f"{topic}.*")
        pubsub_channel.basic_consume(queue=queue_name, on_message_callback=message_callback, auto_ack=True)

    Thread(target=pubsub_channel.start_consuming).start()

    # establish socket connections
    time_start = 0
    can_publish = False
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as socket_left:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as socket_right:

            # bind sockets on both sides
            socket_left.bind((HOST_IP, port_left))
            socket_left.listen()
            socket_right.connect((HOST_IP, port_right))            

            connection, address = socket_left.accept()
            with connection:
                while True:
                    
                    # NOT holding token   
                    if not holds_token:
                        token_message = connection.recv(8)
                        if (token_message == b'_tk_'):
                            holds_token = True
                            print("(info) Token received!")
                            # start counting
                            time_start = time.time()
                    
                    # holding token
                    else:
                        if (time_start == 0):
                            time_start = time.time()

                        topic = input(f"Choose topic {TOPIC_SUBSCRIPTION_DICTIONARY[user_id]}:")
                        message = ""
                        if (topic in TOPIC_SUBSCRIPTION_DICTIONARY[user_id]):
                            message = input(f"[{topic}] <Me>: {message}")
                            can_publish = True
                            #pubsub_channel.basic_publish(exchange="cpdtopicex", routing_key=f"{topic}.{user_id}", body=message)

                        if (time.time() - time_start > 15.0):
                            if (can_publish):
                                print("(info) Sorry, time has passed and the message won`t be sent.")
                                can_publish = False
                            print("(info) Token passed..")
                            socket_right.sendall(b'_tk_') 
                            holds_token = False
                        else:
                            if (can_publish):
                                pubsub_channel.basic_publish(exchange="cpdtopicex", routing_key=f"{topic}.{user_id}", body=message)
                                can_publish = False

###
if __name__ == "__main__":
    main(sys.argv[1], int(sys.argv[2]), int(sys.argv[3]))