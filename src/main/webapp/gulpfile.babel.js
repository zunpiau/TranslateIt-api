import gulp from 'gulp';
import htmlmin from 'gulp-htmlmin';

const path = {
    src: 'src/*.html',
    dist: './view'
};

export function minify() {
    return gulp.src(path.src, {since: gulp.lastRun(minify)})
        .pipe(htmlmin({
            collapseWhitespace: true,
            minifyCSS: true,
            removeComments: true
        }))
        .pipe(gulp.dest(path.dist));
}

export function watch() {
    gulp.watch(path.src, minify);
}