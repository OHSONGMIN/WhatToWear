import Home from "@/pages/Home.vue";
import Login from "@/pages/Login.vue";
import Cart from "@/pages/Cart.vue";
import {createRouter, createWebHistory} from "vue-router";
import Order from "@/pages/Order.vue";
import Orders from "@/pages/Orders.vue";
import History from "@/pages/History.vue";
import Signup from "@/pages/Signup.vue";
import Admin from "@/pages/Admin.vue";
import AdminOutfit from "@/pages/AdminOutfit.vue";
import AdminMember from "@/pages/AdminMember.vue";

const routes = [
    {path: "/", component: Home},
    {path: "/login", component: Login},
    {path: "/cart", component: Cart},
    {path: "/order", component: Order},
    {path: "/orders", component: Orders},
    {path: "/history", component: History},
    {path: "/signup", component: Signup},
    {path: "/admin", component: Admin},
    {path: "/adminOutfit", component: AdminOutfit},
    {path: "/adminMember", component: AdminMember},

]

const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

export default router;