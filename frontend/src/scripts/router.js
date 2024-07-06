import Home from "@/pages/Home.vue";
import Login from "@/pages/Login.vue";
import Cart from "@/pages/Cart.vue";
import {createRouter, createWebHistory} from "vue-router";
import Order from "@/pages/Order.vue";
import Orders from "@/pages/Orders.vue";
import History from "@/pages/History.vue";
import Signup from "@/pages/Signup.vue";

const routes = [
    {path: "/", component: Home},
    {path: "/login", component: Login},
    {path: "/cart", component: Cart},
    {path: "/order", component: Order},
    {path: "/orders", component: Orders},
    {path: "/history", component: History},
    {path: "/signup", component: Signup},
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

export default router;