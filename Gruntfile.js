module.exports = function(grunt) {
	'use strict';
	
	var basePath = 'src/main/webapp/',
		buildPath = 'build/',
		sources = [
			      basePath + 'js/app.js', 
			      basePath + 'js/login.js',
			      basePath + 'js/user/*.js',
			      basePath + 'js/event/*.js'
			];

	grunt.initConfig({
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

  // Default task.
  grunt.registerTask('default', ['jshint']);
};
