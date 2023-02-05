package hr.ferit.dariobarukcic.restorauntapp

data class Order(
    var dishes: ArrayList<Dish>,
    var adress: String,
    var wayOfPaying: String,
    var user:String,
    var id:String?=null
)
