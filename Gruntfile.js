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
					name: 'login',
					baseUrl: basePath + 'js',
					out: buildPath + 'js/login.min.js',
					mainConfigFile : basePath + 'js/config/login.js',
					preserveLicenseComments: false,
					findNestedDependencies : true
				}
			},
			app: {
				options: {
					name: 'app',
					baseUrl: basePath + 'js',
					out: buildPath + 'js/app.min.js',
					mainConfigFile : basePath + 'js/config/app.js',
					preserveLicenseComments: false,
					findNestedDependencies : true
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

  //load plugins
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-contrib-jsdoc');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-requirejs');
  grunt.loadNpmTasks('grunt-bower-task');
  
  //set up shortcut tasks
  grunt.registerTask('default', ['jshint']);
  grunt.registerTask('install', ['bower:install']);
  grunt.registerTask('optimize', ['clean', 'requirejs:login', 'requirejs:app']);
  grunt.registerTask('build', ['jshint', 'jsdoc:dist', 'optimize']);
};
