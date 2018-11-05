CWD ?= $(shell pwd)
NODE_MODULES ?= $(CWD)/node_modules
KCCONF ?= konanc-config
DATKT ?= $(NODE_MODULES)/@datkt

KCFLAGS += $(shell $(KCCONF) crypto.kc --repos --libs)
TEST_KCFLAGS += $(KCFLAGS)
TEST_KCFLAGS += $(shell $(KCCONF) test.kc --repos --libs)

KCC ?= konanc
SRC ?= *.kt

.PHONY: build test klib

build: klib
test: test.kexe
klib: crypto.klib

crypto.klib: $(SRC)
	$(KCC) $(SRC) $(KCFLAGS) -p library -o $@

test.kexe: crypto.klib
	$(KCC) test/*.kt $(TEST_KCFLAGS) -p program -o $@
	./$@

clean:
	rm -f test.kexe crypto.klib
