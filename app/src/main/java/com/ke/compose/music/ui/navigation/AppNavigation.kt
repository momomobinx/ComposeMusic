package com.ke.compose.music.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ke.compose.music.ui.Screen
import com.ke.compose.music.ui.album.detail.AlbumDetailRoute
import com.ke.compose.music.ui.child_comments.ChildCommentsRoute
import com.ke.compose.music.ui.comments.CommentType
import com.ke.compose.music.ui.comments.CommentsRoute
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.login.LoginScreen
import com.ke.compose.music.ui.main.MainRoute
import com.ke.compose.music.ui.playlist_detail.PlaylistDetailRoute
import com.ke.compose.music.ui.playlist_info.PlaylistInfoScreen
import com.ke.compose.music.ui.playlist_list.PlaylistListRoute
import com.ke.compose.music.ui.playlist_new.PlaylistNewScreen
import com.ke.compose.music.ui.share.ShareRoute
import com.ke.compose.music.ui.share.ShareType
import com.ke.compose.music.ui.slpash.SplashScreen
import com.ke.compose.music.ui.users.UsersRoute
import com.ke.compose.music.ui.users.UsersType
import java.net.URLDecoder


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavigationHandler provides {
        navController.navigate(it.createPath())
    }, LocalBackHandler provides {
        navController.popBackStack()
    }) {
        NavigationTree(navController)
    }

}

@Composable
private fun NavigationTree(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.Login.route) {
            LoginScreen {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Screen.Splash.route) {
            SplashScreen {
                navController.navigate(if (it) Screen.Main.route else Screen.Login.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Screen.Main.route) {
            MainRoute({
                navController.navigate(Screen.PlaylistDetail.createUrl(it))
            }, {
                navController.navigate(Screen.PlaylistNew.route)
            })

        }

        composable(Screen.PlaylistDetail.route) {
            PlaylistDetailRoute({
                navController.popBackStack()
            }, {
                navController.navigate(
                    Screen.Comments.createMusicComment(
                        CommentType.Playlist,
                        it.id
                    )
                )
            }, {
                navController.navigate(
                    Screen.Users.createPath(
                        "订阅者",
                        it.id,
                        UsersType.PlaylistSubscribers
                    )
                )
            }) {
                navController.navigate(Screen.PlaylistInfo.createFromPlaylist(it))
            }
        }

        composable(Screen.Comments.route, arguments = listOf(navArgument("id") {
            type = NavType.LongType
        }, navArgument("type") {
            type = NavType.EnumType(CommentType::class.java)
        })) {
            CommentsRoute({
                navController.popBackStack()
            }, { id, type, commentId ->
                navController.navigate(Screen.ChildComment.createPath(type, id, commentId))
            })
        }

        composable(
            Screen.ChildComment.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                },
                navArgument("type") {
                    type = NavType.EnumType(CommentType::class.java)
                }, navArgument("commentId") {
                    type = NavType.LongType
                }
            )

        ) {
            ChildCommentsRoute {
                navController.popBackStack()
            }
        }

        composable(Screen.PlaylistInfo.route) {
            val argument = it.arguments!!
            PlaylistInfoScreen(
                name = argument.getString("name")!!,
                description = URLDecoder.decode(
                    argument.getString("description")!!,
                    Charsets.UTF_8.name()
                ),
                image = argument.getString("image")!!
            ) {
                navController.popBackStack()
            }
        }

        composable(Screen.PlaylistNew.route) {
            PlaylistNewScreen(onBackButtonClick = {
                navController.popBackStack()
            }) {
                navController.popBackStack()
            }
        }

        composable(Screen.Share.route, arguments = listOf(
            navArgument("type") {
                type = NavType.EnumType(ShareType::class.java)
            }, navArgument("id") {
                type = NavType.LongType
            }, navArgument("title") {
                type = NavType.StringType
            }, navArgument("subTitle") {
                type = NavType.StringType
            }, navArgument("cover") {
                type = NavType.StringType
            }
        )) {
            ShareRoute {
                navController.popBackStack()
            }
        }

        composable(Screen.Users.route, arguments = listOf(
            navArgument("title") {
                type = NavType.StringType
            },
            navArgument("id") {
                type = NavType.LongType
            },
            navArgument("type") {
                type = NavType.EnumType(UsersType::class.java)
            }
        )) {
            UsersRoute()
        }

        composable(Screen.AlbumDetail.route, arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            }
        )) {
            AlbumDetailRoute()
        }

        composable(Screen.PlaylistList.route, arguments = listOf(
            navArgument("ids") {
                type = NavType.LongArrayType
            }
        )) {

            PlaylistListRoute(onSelected = { playlistId, songIds ->
                navController.popBackStack()

            }) {
                navController.navigate(Screen.PlaylistNew.route)
            }
        }
    }
}