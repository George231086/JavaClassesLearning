A modification of the producer consumer solution from https://docs.oracle.com/javase/tutorial/essential/concurrency/guardmeth.html.
One thread randomly produces some coordinates (the choice is illuminated in the producers gui), another thread uses these 
coordinates to illuminate a block in its gui. The producer continues until all possible coordinates have been produced, then 
both threads finish.
