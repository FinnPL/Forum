<script>
  let open = true;
  const toggle = () => (open = !open);
</script>

<Modal isOpen={open} {toggle}>
  <ModalHeader {toggle}>Ein Fehler ist Aufgetreten!</ModalHeader>
  <ModalBody>Fehler beim Fetchen</ModalBody>
</Modal>
