# mouse-remoter

This is an application for android mobile to control desktop (moving and left clicking) remotely.

Architecture:
1. Destopper: a java application running on desktop to receive control data from a mobile phone, currently use UDP protocol to communicate.
2. Phonner: a android client app to scanning desktops, laptops in LAN network, touch on mobile to control mouse on the host.
