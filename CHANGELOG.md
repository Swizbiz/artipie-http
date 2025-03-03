## v1.1.3

 - ff0bd9a - fix(multipart): use shared thread pool (#442)
   by Kirill <g4s8.public@gmail.com>
 - 9236082 - deps: bump vertx-web-client from 4.2.1 to 4.2.5 (#436)
   by dependabot[bot] <49699333+dependabot[bot]@users.noreply.github.com>
 - 3472bb2 - deps: bump jetty-server from 10.0.7 to 10.0.8 (#440)
   by dependabot[bot] <49699333+dependabot[bot]@users.noreply.github.com>
 - e27879d - deps: bump jetty-servlet from 10.0.7 to 10.0.8 (#439)
   by dependabot[bot] <49699333+dependabot[bot]@users.noreply.github.com>
 - e0d8e35 - deps: bump httpclient5-fluent from 5.1.2 to 5.1.3 (#438)
   by dependabot[bot] <49699333+dependabot[bot]@users.noreply.github.com>
 - 9848a4a - deps: bump asto from v1.9.0 to v1.11.0 (#437)
   by Kirill <g4s8.public@gmail.com>
 - bb6159a - fix: use shared thread pool for multipart (#435)
   by kirill-h5i <72436338+kirill-h5i@users.noreply.github.com>
 - 31a92fd - test(multipart): multipart - storage integratin (#433)
   by Kirill <g4s8.public@gmail.com>
 - 9842050 - deps: bump vertx-server from 0.5 to 1.0.0 (#431)
   by dependabot[bot] <49699333+dependabot[bot]@users.noreply.github.com>
 - 340d89b - fix: RsHasHeader should check one header among multiple ones. (#427)
   by Denis Garus <garus.d.g@gmail.com>
 - 6a81452 - feat: return a list of the files present in a directory (#415)
   by Denis Garus <garus.d.g@gmail.com>

## v1.1.1

 - 5daf8ca - fix(multipart): remove init multipart flag
   by Kirill <g4s8.public@gmail.com>
 - 028c569 - test: reproduce multipart body corruption
   by Kirill <g4s8.public@gmail.com>
 - c6309c8 - test(multipart): integration test for multipart
   by Kirill <g4s8.public@gmail.com>

## v1.1.0

 - 66a3270 - feat(multipart): inspect method for RqMultipart (#419)
   by Kirill <g4s8.public@gmail.com>
 - 4074d32 - feat: add RqParams values method (#416)
   by Yuurga <yuurga@gmail.com>
 - 1a64baa - feat: added `WithGzipSlice` (#408)
   by Alena <olena.gerasimova@gmail.com>
 - a512977 - feat: add GzipSlice (#405)
   by Alena <olena.gerasimova@gmail.com>
 - bffdac4 - docs: add baudoliver7 as contributor (#403)
   by Olivier Baudouin OURA <baudoliver7@gmail.com>

## v0.26

 - 03a19bd deps: bump asto from v1.5.1 to v1.8.0 (#395)
 - ee959a3 deps: bump matchers-json from 1.0.3 to 1.4.0 (#389)
 - cd020d4 deps: bump jetty-servlet from 10.0.6 to 10.0.7 (#393)
 - e350e43 deps: bump jetty-server from 10.0.6 to 10.0.7 (#394)
 - 812f635 feat: override toString in Authentication.User (#390)
 - 87a62b6 deps: bump vertx-web-client from 4.1.4 to 4.1.5 (#392)
 - b9c2342 feat(matchers): matching body with string matcher
 - 191f445 test(multipart): added test for conda payload (#386)
 - a8a4014 deps: bump junit-jupiter-engine from 5.8.0 to 5.8.1 (#385)

## v0.25

 - fix: parse colon in multipart headers correctly (#384)

## v0.24

 - deps: bump slf4j-simple from 1.7.31 to 1.7.32 (#351)
 - deps: bump vertx-web-client from 4.1.3 to 4.1.4 (#378)
 - deps: bump junit-jupiter-engine from 5.7.2 to 5.8.0 (#375)
 - fix: multipart request body parsing
 - deps: bump asto from v1.3.4 to v1.5.1 (#379)

## v0.23.1

 - fix: MultipartHeaders: header values are not case-insensitive (#376) 

## v0.23

 - feat: added rq line to AuthScheme and token auth header (#372) 
 - deps: bump vertx-web-client from 4.1.1 to 4.1.3 (#370)

## v0.22

 - feat: add multipart filter method (#368)

## v0.21

 - feat: added optional slice (#364)
 - fix: Corrected short license link (#365)

## v0.20

 - fix: parse accept header correctly (#361)
 - deps: bump mime from 2.0.0 to v2.1 (#353)
 - fix: duplicate buffers of RsWithBody (#359)
 - test: added IT for slices (#355)
 - feat: finished multipart request implementation
 - deps: bump asto from v1.3.0 to v1.3.2 (#347)
 - deps: bump jetty-server from 10.0.5 to 10.0.6 (#342)
 - ci: fixed release script
 - deps: bump asto from 1.2.1 to v1.3.0 (#345)

## v0.19

 - ci: Update dependabot.yml
 - deps: Bump jetty-servlet from 10.0.0 to 10.0.6
 - deps: Bump vertx-web-client from 4.1.0 to 4.1.1 (#341)
 - deps: Bump slf4j-simple from 1.7.30 to 1.7.31 (#338)
 - deps: Bump asto from 1.2.0 to 1.2.1 (#337)
 - deps: Bump jetty-server from 10.0.1 to 10.0.5 (#335)
 - feature(multipart): Added reactive processor tokenizer
 - feature(multipart): Multipart request part implementation
 - deps: Bump asto from 1.1.0 to 1.2.0 (#326)
 - test: Response matcher for JSON
 - deps: Bump vertx-web-client from 4.0.3 to 4.1.0 (#323)
 - deps: Bump vertx-web-client from 3.8.5 to 4.0.3 (#315)
