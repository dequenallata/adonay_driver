import 'package:flutter/material.dart';
import 'package:adonay_driver/providers/theme_provider.dart';
import 'package:adonay_driver/styles/styles.dart';

class ToggleThemeWidget extends StatelessWidget {
  const ToggleThemeWidget({
    super.key,
    required this.themeProvider,
  });

  final ThemeProvider themeProvider;

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        themeProvider.toggleTheme();
      },
      child: Container(
        decoration: BoxDecoration(
          borderRadius: BorderRadius.circular(8),
        color: themeProvider.isDarkTheme 
        ? page
        : Color.fromRGBO(51, 51, 51, 1)
        
        ),
        width: 40,
        height: 40,
        child: themeProvider.isDarkTheme ?
        Icon(Icons.light_mode, color: Color.fromRGBO(51, 51, 51, 1),)
        : Icon(Icons.dark_mode, color: page,),
      ),
    );
  }
}
