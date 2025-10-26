import {MatSnackBar} from '@angular/material/snack-bar';

export function showError(err: unknown, fallback: string, dialog:MatSnackBar) {
  console.log("error",err)
  const status = (err as any)?.error.status;
  const serverMsg =(err as any)?.error?.message

  const msg = `${fallback}: ${serverMsg}${status ? ` (HTTP ${status})` : ''}`

  dialog.open(msg, 'Close', {
    duration: 4000,
    horizontalPosition: 'center',
    verticalPosition: 'top'
  });
}
