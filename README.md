# pal-tracker
Lab assignments for Pivotal Architect Lab

## Lab: Scaling an App on Cloud Foundry

Cloud Foundry supports two types of scaling:
* Vertical scaling: Each app instance has more memory or disk space.
* Horizontal scaling: There are more app instances serving requests.

Scaling can be done through command line by using the CF CLI.
The command `cf scale` controls both horizontal and vertical scaling.

Scaling can also be done through the Pivotal Console portal.
* Application -> Overview -> Enable Autoscaling
* Add appropriate rules to scale down from minimum instances to maximum instances
