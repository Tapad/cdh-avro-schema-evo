package:
	-@test -e package.sh && ./package.sh 2> /dev/null || true

datagen: package clean
	@./datagen.sh

conv: datagen
	-@./conv.sh

evo: conv
	-@./evo.sh

clean:
	@./clean.sh

distclean: clean
	-@find ../build -mindepth 1 -delete
