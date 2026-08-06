# Bill Generator App(IBill)

A comprehensive Android application for managing shop sales, inventory, and order billing. This app allows shop owners to track daily income, manage stock, register clients, and generate orders efficiently using barcode scanning.

## 🚀 Features

- **User Authentication**: Secure Login and Registration for shop owners.
- **Dashboard (Invoice Fragment)**: 
    - Real-time metrics: Today's Income, Pending Orders, Total Invoices, and Low Stock alerts.
    - Recent transactions list with status tracking.
    - Swipe-to-refresh for latest data updates.
- **Inventory Management**:
    - Add, view, and edit items in the inventory.
    - Track unit prices, MRP, and discount rates.
- **Order System**:
    - **Barcode Scanning**: Integrated ZXing scanner for quick item entry during billing.
    - **Real-time Billing**: Calculate grand totals, adjust quantities, and apply discounts on the fly.
    - **Finalization**: Review final orders and update payment status (Pending/Paid).
- **History**: Searchable history of all past orders.
- **Client Management**: Maintain a database of client details.
- **PDF Generation**: Support for generating bills from XML layouts.

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI Architecture**: MVVM (Model-View-ViewModel) with View Binding.
- **Networking**: Retrofit 2 with OkHttp for REST API communication.
- **Local Storage**: Room Database for caching and offline support.
- **Dependency Injection**: Manual injection with Factory patterns for ViewModels.
- **Utilities**: 
    - `SwipeRefreshLayout` for data refreshing.
    - `ZXing` for barcode/QR code scanning.
    - `Android-XML-to-PDF-Generator` for generating digital bills.

## 📂 Project Structure

- **Activities**: Contains the main entry points (`MainActivity`), authentication flow (`LoginActivity`, `RegisterUserActivity`), and specialized workflows (`OrderActivity`, `FinalOrderActivity`).
- **Fragments**: Modular UI components for the Dashboard, Inventory, Clients, and History.
- **ViewModels**: Business logic and data management for each screen, ensuring UI remains reactive.
- **Api**: Retrofit service interfaces, API configuration, and data repositories.
- **Roomdb**: Entity definitions, DAOs, and Database helpers for local persistence.
- **Adapters**: RecyclerView and ListView adapters for displaying lists of orders and items.
- **UtilClasses**: Shared helper functions for formatting, shared preferences, and UI navigation.

## 🔄 App Flow

1. **Splash**: Initial loading screen.
2. **Auth**: Users log in or register their shop.
3. **Home**: View business metrics and recent transactions.
4. **New Order**: 
   - Click the "Add" button in the navigation bar.
   - Scan item barcodes to populate the invoice.
   - Set client name and save.
5. **Finalize**: Review the order, process payment, and generate a bill.
6. **Management**: Use the navigation tabs to update inventory or view client history.

## ⚙️ Setup & Configuration

- **Minimum SDK**: 23
- **Target SDK**: 34 (Android 14)
- **Base URL**: The app communicates with a remote backend hosted at Railway (configurable in `ApiConfig.kt`).

---
*Developed as a robust solution for small to medium-scale retail management.*
