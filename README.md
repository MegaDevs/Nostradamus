Nostradamus
===========

When and Where
------------

And yet again we realized that we were going cold turkey for hackathons. We simply couldn’t wait to get to the next one. This time the [WhyMCA](http://www.whymca.org/) team had the very nice idea of collaborating with [Codemotion](http://rome.codemotionworld.com/) in order to give the hackers a suggestive location for them to create new awesomeness. What better place of [H-Farm](http://www.h-farmventures.com/en/)?

The hackaton started at 10AM on 17th November and it was pretty interesting all in all, especially some talks regarding Android security matters and NoSQL databases. But the real fun began at 8PM of that very same evening. The hackathon started. The developers had 22 hours (more or less) to build somethig, anything, that could help in case of natural disasters or emergencies. 

What
----

The topics were:

+ coordination of rescue teams
+ gathering of donations (both material and economical)
+ help for people in danger

Our team resembled the one that we showed at the previous hackathon in Bologna: Sebastiano, Dario, Stefano, Nicola (live from Berlin, thank you Skype, even if you’re now being messed up by Microsoft). But this time we foresaw that we needed someone with high designing skills and some kind of “taste” when it comes to aesthetics. So we asked Roberta to join us.

We decided to build Nostradamus, a 360 degrees emergency suite which covered three kind of situations, where each one is a different moment of a disaster:

+ before the disaster
+ during the disaster
+ after the disaster

How
---

First of all, let’s see what happens before a disaster. Since everything is going well, people is still working and doing their business, for instance, in a building. It may happen that the building has a surveillance system which Nostradamus can use to detect if, at the time of the beginning of the disaster, there are human beings inside the building. It does so by performing some computer vision operations in order to do what’s called “face detection”, thanks to the [ReKognition APIs](http://rekognition.com/). Moreover, since it can detect whether there is someone in trouble in a particular building, it takes advantage of a bayesian network which dynamically calculates how to distribute the rescue teams (it makes no sense at all to send the ambulance to an empty building, doesn’t it?).

Let’s assume that in a specific geographical area disasters are pretty frequent. Let’s also assume that citizens are willing to install an application on their Android smartphones which could really help them in dangerous situations, for the sake of prevention. When a disaster happens, the civil defense may automatically warn those citizens whose smartphone is equipped with the application, and here’s what happens. The smartphone goes to a so called “emergency” mode, which triggers the activation of Bluetooth through which it broadcasts its position to eventual nearby devices. An opportunistic network is hence being created: devices who don’t know each other may communicate and share their respective location.. Until a rescuer walks nearby with its “rescue” version of the application: the latter can detect nearby Bluetooth “emergency” devices and their location, so that rescue can be performed; those locations are also transmitted to the civil defense’s servers.

What about coordination of rescue teams? The whole thing is done by calling each other via cellphones or using CBs (CityBand frequencies). So we built a prototype of a passive device which is meant to be placed in front of disaster-affected buildings or locations and which contains a small NFC tag. Each rescue team can write their operations on the tag and read what other teams did from the same tag. The tag is encapsulated in a water resistant shell, which also features notification LEDs powered by a small battery; the battery is recharged by a small set of solar panels.



The slides of our presentation can be found [here](https://github.com/MegaDevs/Nostradamus/raw/master/presentazione-nstrdms.pdf).

So What?
----------

So: a Ruby frontend, an AppEngine backend, two Android applications and various AI and ComputerVision stuff, all together: did it worked?

Sure it did. It all worked well at the presentation.

Did we win? Nope, unlike last time in Bologna we managed to achieve an honest 2nd place, even if we were hoping for the 1st place. Why second place then? Because, according to the WhyMCA team, we (willingly) refused to use any of the sponsored APIs. Well, we wanted to do all on our own, as we proudly did.
