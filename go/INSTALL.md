# Building executables of `redacted`

To build the executables in 64-bits from this Go code (assuming you are on MacOS):

_(for MacOS)_
```console
$ GOOS=darwin GOARCH=amd64 go build -o bin/redacted main.go
```

_(for Linux)_
```console
$ CGO_ENABLED=1 GOOS=linux GOARCH=amd64 CC="x86_64-linux-musl-gcc" go build -o bin/redacted-linux --ldflags '-w -linkmode external -extldflags "-static"' main.go
```

_(for Windows)_
```console
$ CGO_ENABLED=1 GOOS=windows GOARCH=amd64 CC="x86_64-w64-mingw32-gcc" go build -o bin/redacted.exe main.go
```


<hr />
&copy; 2023 Cyril Dever. All right reserved.