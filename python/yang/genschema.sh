#!/bin/bash
yang2dsdl models/$1.yang

# prettyify
find . -type f \( -name "*.dsrl" -or -name "*.rng" -or -name "*.sch" \) -print0 | while IFS= read -r -d $'\0' fname; do
	echo "prettyify $fname"
	xmllint --format $fname > t && mv t $fname
done