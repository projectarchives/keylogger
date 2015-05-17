# jRAT Keylogger Plugin

Cross platform Key Stroke Capture Plugin which both works live and offline, sorting after year, month and day

| Information	| Value
| ---           |:---
| Name			| Keylogger
| Author     	| jRAT
| Description   | Keylogger Plugin
| Version		| 1.1

## Packets

| Packet Name	| Header
| ---           |:---
| STATUS		| 123
| LOGS			| 124
| LOG			| 125

## Dependencies

- Client
	- [API](https://github.com/java-rat/jrat-api)
	- [iconlib](https://github.com/redpois0n/iconlib)

- Stub
	- [Stub API](https://github.com/java-rat/jrat-stub-api)
	- [JNativeHook](https://github.com/kwhat/jnativehook)
	- [jna](https://github.com/twall/jna) (If we want to show current window, Windows only)
