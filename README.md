# Guarding Problem Tool

This program is a tool used to help find the guarding number of a polygon. Any simple polygon for which g(P) = w(P) this polygon will be able to use Squeeze Theorem to help the user find.

The program has 3 stages, the DRAWING stage, the WITNESS stage and the GUARD stage.

In the DRAWING stage, users draw the polygon similarly to the other programs, left-click to add a point, right click to remove.

In the WITNESS stage, users can add and remove independent witness points inside the polygon. If a user tries to add two witnesses who see the same region, an error notifies them as such.

In the GUARD stage, users can add and remove guards to see if they can see the whole polygon with the same number of guards as they had independent witnesses.
By Squeeze Theorem, the user will have found the guarding number of the polygon.