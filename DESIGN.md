# Design

**Schets**
<p align="center">
     <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/overzicht.jpg" height="80%" width="80%"/>
</p>

*Activities:*
1. ProductActivity.java, activity_product.xml, product_item.xml  
2. UserActivity.java, activity_user.xml, user_item.xml  
5. ProfileActivity.java, activity_profile.xml  
8. MenuActivity.java, activity_menu.xml  
9. AddProductActiviy.java, activity_addProduct.xml
10. AddUserActivity.java, activity_addUser.xml

*External Component: * SQLite


**UML**
<p align="center">
     <img src="https://github.com/AnneHS/Streeplijst/blob/master/app/doc/uml2.PNG" height="80%" width="80%"/>
</p>

**Streeplijst.db: tabellen**  
*streeplijst:* id, name, drawableID, costs  
*products:* id, name, price  
*transactions:* userID, productName, productPrice, timestamp  
