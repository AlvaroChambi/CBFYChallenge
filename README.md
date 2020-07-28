# Challenge

In order to solve this challenge I'm taking the following steps.
- Define the requirements and the UX of the app
- Given the requirements define an architecture for the app
- Define an MVP
- Break down functionality into tasks

# Requirements and UX
We're going to build this app around three key concepts. First, it's ment to sell things, second,we're trying to know the impact of discounts and if one strategy is better than other and last, the store is projected to grow, so it should be easy to add new functionality.

## Requirements:
1. The user should be presented every product that is available:

Our aim is to sell products, so the first thing we have to do is present those products to the user, every product should be ‘attractive’ and therefore any info that would encourage the user to pick an item should also be available right away.
Also, now there are only 3 items, but if this should be scalable, the user should also be able to search for what he wants

2. The user should be able to check all the relevant info regarding the products that are presented:

So, the relevant info we have right now is a name, a price, and a discount that can be applied upon certain conditions on certain products.
Again thinking on scalability we should assume that the relevant info about the product can be expanded and therefore take that on our design considerations.

3. The user should be able to order any product that is available:

The user should have an easy access to order any available product in any quantity, and any relevant info related with the order should be presented clearly

4. The user should be presented every product that has currently selected and any information related to the selection:

So, this is very important information to the user, at least a hint of the state of their orders should be visible right away, without the need of any interaction, and more detailed info should be 1 click away.
The detailed info should show info related with the sum of the products selected, that means discounts, and since one of our main goals is to promote discounts, if there’s a product that has a discount associated but has not met the requirement it should be reminded to the user.
Price of every item and total price should be displayed in a way that is easy to understand.

# Architecture definition

Based on the fact that the app should be easy to scale and be open to changes, I'm going to base my desing on a clean architecture, it'll allow us to structure our software in layers isolating modules from each another. With this approach we can easily implement unit tests for each individual module, and any change on a module won't affect the rest. A good test coverage and a controlled scope changes on the code will make implementing new features and maintenance easier.
Using a well know architecture will also help working with the development team, it'll makes the code and design decision easier to understant for everyone.

## Presentation layer
For the presentation layer, I'm going to implement the MVP pattern, confort zone, I know it can successfully comply with our requirements
Threading will be implemented using executors. Handlers and activity/fragment lifecycle will be used to allow the presentation layer to request and present domain data.
For the navigation, we can handle our design using activities as containers and one frament per activity. Activities will be used to handle the navigation with the default navigation stack.
Presenter maps domain model to a presentation model that will be displayed on the fragment.
Fragment will handle it's own lifecycle, it'll decide when to request data, will provide callbacks in order to save and restore the view state when needed.


## Domain layer
The domain layer will consist on use cases, use cases will request the raw data, map it to our bussiness model and handle cache on memory.

## Data layer
For the data layer I'm implementing the repository pattern which will provide us and interface that is agnostic of where the data comes from, in this case a sqlite database or from a remote connection.
Data from remote will be cached on the database, a cache invalidation policy based on a server should be implemented.

## Third party libraries
Retrofit

Dagger

Room


## Notes
- Using the MVVM pattern, something based more on observables than observers, would have simplified showing data that is meant to be changed, like the cart products count. While the MVP observer pattern can solve the problem, using observables makes it trivial. With our current implementation going from MVP to MVVM shouldn't take long, changes will only concern the presentation layer.
