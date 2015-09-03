package:
	-@test -e package.sh && ./package.sh 2> /dev/null || true

datagen: package
	@./datagen.sh

conv: datagen
	@./conv.sh

evo: conv
	@./evo.sh
