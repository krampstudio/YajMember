module.exports = function(grunt) {
	'use strict';
	
	var basePath = 'src/main/webapp/',
		buildPath = 'build/',
		sources = [
			      basePath + 'js/app.js', 
			      basePath + 'js/login.js',
			      basePath + 'js/user/*.js',
			      basePath + 'js/event/*.js',
				  basePath + 'js/helpers/*.js'
			];

	grunt.initConfig({
		bower: {
			install: {
				options: {
					targetDir: basePath + 'js/lib',
					cleanup: true,
					install: true
				}
			}
		},
		
		clean: [buildPath + 'js'],
		
		requirejs: {
			login: {
				options: {
					appDir: basePath + 'js',
					baseUrl: './',
					dir: buildPath + 'js',
					preserveLicenseComments: false,
					modules: [{
						name: 'login',
						mainConfigFile : 'config/login.js'
					}]
				}
			}
		},
		jsdoc : {
			dist: {
				src: sources,
				dest: buildPath+ 'reports/jsdoc'
			}
		},
		jshint : {
			files : sources,
			options: {
				browser : true,
				smarttabs : true
			},
			globals: {
				jQuery: true
			}
		}
  });

  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-jsdoc');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-requirejs');
  grunt.loadNpmTasks('grunt-bower-task');
  
  // Default task.
  grunt.registerTask('default', ['jshint']);
  grunt.registerTask('install', ['bower:install']);
  grunt.registerTask('optimize', ['clean', 'requirejs:login']);//, 'requirejs:app']);
  
  grunt.registerTask('build', ['jshint', 'jsdoc:dist', 'optimize']);
};
