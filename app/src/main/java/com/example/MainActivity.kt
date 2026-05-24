package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.AppDatabase
import com.example.data.CrmRepository
import com.example.ui.CrmViewModel
import com.example.ui.CrmViewModelFactory
import com.example.ui.screens.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClosiraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BgApp
                ) {
                    ClosiraApp()
                }
            }
        }
    }
}

@Composable
fun ClosiraApp() {
    val context = LocalContext.current
    val navController = rememberNavController()

    // Constructor Dependency Injection for Room Database Components
    val database = remember { AppDatabase.getDatabase(context.applicationContext) }
    val repository = remember {
        CrmRepository(
            database.leadDao(),
            database.escalationDao(),
            database.followUpDao()
        )
    }
    
    val factory = remember { CrmViewModelFactory(repository) }
    val viewModel: CrmViewModel = viewModel(factory = factory)

    // Main Stack Router
    NavHost(
        navController = navController,
        startDestination = "main_tabs"
    ) {
        // Tabbed section enclosing Home, Leads, Escalations, FollowUps
        composable("main_tabs") {
            var currentTab by remember { mutableStateOf("home") }

            Scaffold(
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF211F26))
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        Divider(
                            color = OutlineVariantColor,
                            thickness = 1.dp,
                            modifier = Modifier.fillMaxWidth()
                        )
                        NavigationBar(
                            containerColor = Color(0xFF211F26),
                            tonalElevation = 0.dp,
                            modifier = Modifier.height(68.dp)
                        ) {
                            val tabs = listOf(
                                NavigationTab("home", "Home", Icons.Default.Home),
                                NavigationTab("leads", "Leads", Icons.Default.People),
                                NavigationTab("escalations", "Escalations", Icons.Default.Warning),
                                NavigationTab("followups", "Follow-ups", Icons.Default.CalendarToday)
                            )

                            tabs.forEach { tab ->
                                val selected = currentTab == tab.route
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = { currentTab = tab.route },
                                    icon = {
                                        Icon(
                                            imageVector = tab.icon,
                                            contentDescription = tab.label,
                                            tint = if (selected) OnPrimaryContainerColor else OnSurfaceVariantColor
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.label,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (selected) PrimaryColor else OnSurfaceVariantColor
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = PrimaryContainerColor,
                                        selectedIconColor = OnPrimaryContainerColor,
                                        unselectedIconColor = OnSurfaceVariantColor,
                                        selectedTextColor = PrimaryColor,
                                        unselectedTextColor = OnSurfaceVariantColor
                                    )
                                )
                            }
                        }
                    }
                },
                containerColor = BgApp,
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    when (currentTab) {
                        "home" -> HomeScreen(
                            viewModel = viewModel,
                            onNavigateToTab = { currentTab = it },
                            onNavigateToLeadDetail = { leadId -> 
                                navController.navigate("conversation_detail/$leadId")
                            }
                        )
                        "leads" -> LeadsScreen(
                            viewModel = viewModel,
                            onNavigateToLeadDetail = { leadId ->
                                navController.navigate("conversation_detail/$leadId")
                            }
                        )
                        "escalations" -> EscalationsScreen(
                            viewModel = viewModel,
                            onNavigateToLeadDetail = { leadId ->
                                navController.navigate("conversation_detail/$leadId")
                            }
                        )
                        "followups" -> FollowUpsScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }

        // Details screen pushed onto top of the stack (automatically hides navigation bar)
        composable("conversation_detail/{leadId}") { backStackEntry ->
            val leadId = backStackEntry.arguments?.getString("leadId") ?: ""
            ConversationDetailScreen(
                viewModel = viewModel,
                leadId = leadId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

private data class NavigationTab(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
