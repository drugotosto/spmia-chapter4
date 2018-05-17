#!/usr/bin/env bash
echo "Eseguo il building del commit che è stato TAGGATO con: $BUILD_NAME ..."
echo "Con le configurazioni attuali di travis (SUDO) il building viene eseguito su una macchina in remoto (GCE)."
echo "In questo script possono essere aggiunti anche i comandi che fanno partire i vari UNIT/INTEGRATION TESTS"

mvn clean package docker:build
