module.exports = function(grunt) {
	'use strict';
	
	var basePath = 'src/main/webapp/',
		buildPath = 'build/',
		staticPath = buildPath + 'static/',
		sources = [
			      basePath + 'js/app.js', 
			      basePath + 'js/login.js',
			      basePath + 'js/user/*.js',
			      basePath + 'js/event/*.js',
				  basePath + 'js/helpers/*.js'
			];

	grunt.initConfig({
		pkg : grunt.file.readJSON('package.json'),	
		bower: {
			install: {
				options: {
					targetDir: basePath + 'js/lib',
					cleanup: true,
					install: true
				}
			}
		},
		
		clean: [staticPath],
		
		requirejs: {
			login: {
				options: {
					name: 'login',
					baseUrl: basePath + 'js',
					out: staticPath + 'js/login.min.js',
					mainConfigFile : basePath + 'js/config/login.js',
					preserveLicenseComments: false,
					findNestedDependencies : true
				}
			},
			app: {
				options: {
					name: 'app',
					baseUrl: basePath + 'js',
					out: staticPath + 'js/app.min.js',
					mainConfigFile : basePath + 'js/config/app.js',
					preserveLicenseComments: false,
					findNestedDependencies : true
				}
			}
		},
		
		htmlrefs:{
			dist: {
				src:  basePath + '*.html',
				dest: staticPath
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
				jshintrc: basePath + 'js/.jshintrc'
			}
		}
  });

  //load plugins
  grunt.loadNpmTasks('grunt-contrib-jshint');
  grunt.loadNpmTasks('grunt-jsdoc');
  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-contrib-requirejs');
  grunt.loadNpmTasks('grunt-bower-task');
  grunt.loadNpmTasks('grunt-htmlrefs');
  
  //set up shortcut tasks
  grunt.registerTask('default', ['jshint']);
  grunt.registerTask('install', ['bower:install']);
  grunt.registerTask('optimize', ['requirejs:login', 'requirejs:app']);
  grunt.registerTask('build', ['jsdoc:dist', 'optimize', 'htmlrefs']);
};
